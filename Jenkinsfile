pipeline {
  agent any

  environment {
    APP_NAME = "demo"
    IMAGE_TAG = "latest"
    K8S_MANIFEST_DIR = "k8s"
  }

  stages {
    stage('Checkout') {
      steps {
        // récupère le code depuis ton repo GitHub
        checkout([
          $class: 'GitSCM',
          branches: [[name: '*/main']],
          userRemoteConfigs: [[
            url: 'https://github.com/hichem-samandi/demo.git',
            credentialsId: 'github-creds'
          ]]
        ])
      }
    }
    stage('Check Java') {
      steps {
                    sh 'java -version'
                    sh 'javac -version'
                    sh 'mvn -v'
                }
            }

    stage('Build & Test') {
      steps {
        sh 'mvn -B clean package -DskipTests=false'
      }
    }

    stage('Build Docker image in Minikube') {
      steps {
        sh '''
          echo "==> Using Minikube Docker daemon"
          eval $(minikube -p minikube docker-env)
          docker build -t ${APP_NAME}:${IMAGE_TAG} .
          echo "==> Image built successfully"
          docker images | grep ${APP_NAME}
        '''
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        sh '''
          echo "==> Deploying ${APP_NAME}:${IMAGE_TAG}"
          sed "s|REPLACE_IMAGE|${APP_NAME}:${IMAGE_TAG}|g" ${K8S_MANIFEST_DIR}/deployment.yaml > /tmp/deployment.yaml
          kubectl apply -f /tmp/deployment.yaml
          kubectl apply -f ${K8S_MANIFEST_DIR}/service.yaml
          kubectl rollout status deployment/${APP_NAME} --timeout=90s
        '''
      }
    }


/*     stage('Smoke Test') {
      steps {
        sh '''
          NODEPORT=$(kubectl get svc spring-boot-rest-service -o jsonpath='{.spec.ports[0].nodePort}')
          IP=$(minikube ip)
          echo "Testing endpoint: http://${IP}:${NODEPORT}/api/hello"
          for i in 1 2 3; do
            sleep 5
            curl -f http://${IP}:${NODEPORT}/api/hello && break || echo "Retrying..."
          done
        '''
      }
    } */
  }

  post {
    always {
      echo "Pipeline complete — showing current pods:"
      sh 'kubectl get pods -o wide'
    }
  }
}
